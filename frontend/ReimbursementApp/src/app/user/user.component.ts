import { Component, OnInit } from '@angular/core';
import { AbstractControl, FormArray, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ClaimService } from '../services/claim.service';
import { ActivatedRoute } from '@angular/router';
import { ClaimRequest } from '../common/claim-request';
import { LimitsConfiguration } from '../common/limits-configuration';
import { LimitsConfigurationService } from '../services/limits-configuration.service';

@Component({
  selector: 'app-user',
  templateUrl: './user.component.html',
  styleUrls: ['./user.component.css']
})
export class UserComponent implements OnInit {
  userId: number = 0;
  availableReceiptTypes: string[] = [];
  limitsConfiguration: LimitsConfiguration | undefined;
  receiptLimitsMap: { [key: string]: number } = {};
  isReady: Boolean = false;

  claimForm!: FormGroup;

  constructor(
    private fb: FormBuilder,
    private route: ActivatedRoute,
    private claimService: ClaimService,
    private limitsConfigurationService: LimitsConfigurationService
  ) { }

  async ngOnInit() {
    this.route.queryParams.subscribe(params => {
      this.userId = +params['id'] || 0;
    });
    await this.getLimitsConfiguration();

    this.setForm();
    console.log(this.limitsConfiguration?.carMileageRate);
    this.isReady = true;
  }

  private setForm() {
    this.claimForm = this.fb.group({
      startDate: ['', Validators.required],
      endDate: ['', Validators.required],
      receipts: this.fb.array([]),
      reimbursedDaysWithAllowance: [[]],
      distanceInKMForCarMileage: [0],
      carPlates: [''],
      totalReimbursementAmount: [0]
    });
    const receiptsFormArray = this.claimForm.get('receipts') as FormArray;

    for (const receiptType in this.receiptLimitsMap) {
      if (this.receiptLimitsMap.hasOwnProperty(receiptType)) {
        const receiptControl = this.fb.control('', [Validators.required, this.validateReceiptValue(receiptType)]);
        receiptsFormArray.push(receiptControl);
      }
    }
  }

  private validateReceiptValue(receiptType: string) {
    return (control: AbstractControl) => {
      const limit = this.receiptLimitsMap[receiptType];
      const value = control.value;

      if (value > limit) {
        return { 'exceedsLimit': true };
      }

      return null;
    };
  }

  // private getLimitsConfiguration() {
  //   this.limitsConfigurationService.limitsConfiguration.subscribe(
  //     (config) => {
  //       if (config !== null) {
  //         this.limitsConfiguration = config;
  //         this.availableReceiptTypes = Object.keys(this.limitsConfiguration.receiptLimits);
  //       }
  //     }
  //   );
  // }
  private async getLimitsConfiguration() {
    const response = await this.limitsConfigurationService.getLimitsConfiguration();
    if (response.status === 200 && response.body) {
      this.limitsConfiguration = response.body;
      this.receiptLimitsMap = this.limitsConfiguration.receiptLimits;
    }
  }


  onSubmit() {
    const formData = this.claimForm.value;
    const claimRequest: ClaimRequest = {
      startDate: formData.startDate,
      endDate: formData.endDate,
      receipts: formData.receipts,
      reimbursedDaysWithAllowance: formData.reimbursedDaysWithAllowance,
      distanceInKMForCarMileage: formData.distanceInKMForCarMileage,
      carPlates: formData.carPlates,
      totalReimbursementAmount: formData.totalReimbursementAmount,
      userId: this.userId
    };

    this.claimService.saveClaim(claimRequest);
  }
}