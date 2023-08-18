import { Component, OnInit } from '@angular/core';
import { FormGroup, FormBuilder, Validators, FormArray } from '@angular/forms';
import { LimitsConfiguration } from '../common/limits-configuration';
import { LimitsConfigurationService } from '../services/limits-configuration.service';

@Component({
  selector: 'app-admin',
  templateUrl: './admin.component.html',
  styleUrls: ['./admin.component.css']
})
export class AdminComponent implements OnInit {
  limitsForm: FormGroup = this.fb.group({}); 
  limitsConfiguration: LimitsConfiguration = new LimitsConfiguration();

  constructor(
    private fb: FormBuilder,
    private limitsConfigService: LimitsConfigurationService
  ) { }

  async ngOnInit() {
    await this.loadLimitsConfiguration();
    this.initializeForm();
  }

  private async loadLimitsConfiguration() {
    const response = await this.limitsConfigService.getLimitsConfiguration();
    if (response.status === 200 && response.body) {
      this.limitsConfiguration = response.body;
    }
  }

  private initializeForm() {
    console.log('Setting dailyAllowanceRate:', this.limitsConfiguration.dailyAllowanceRate);

    this.limitsForm = this.fb.group({
      dailyAllowanceRate: [this.limitsConfiguration.dailyAllowanceRate, Validators.required],
      carMileageRate: [this.limitsConfiguration.carMileageRate, Validators.required],
      totalReimbursementLimit: [this.limitsConfiguration.totalReimbursementLimit, Validators.required],
      mileageLimitInKilometers: [this.limitsConfiguration.mileageLimitInKilometers, Validators.required],
      receiptLimits: this.buildReceiptLimitsFormArray(this.limitsConfiguration.receiptLimits)
    });
    console.log(this.limitsForm?.get('dailyAllowanceRate')?.value);
  }

  private buildReceiptLimitsFormArray(receiptLimits: { [key: string]: number }): FormArray {
    const receiptControls = Object.keys(receiptLimits).map(receiptType => {
      return this.fb.group({
        type: [receiptType, Validators.required],
        limit: [receiptLimits[receiptType], Validators.required]
      });
    });

    return this.fb.array(receiptControls);
  }

  public get receiptControls() {
    const receiptLimitsFormArray = this.limitsForm.get('receiptLimits') as FormArray;
    return receiptLimitsFormArray.controls;
  }

  addReceiptType() {
    const receiptType = this.fb.group({
      type: ['', Validators.required],
      limit: [0, Validators.required]
    });
    this.receiptTypes.push(receiptType);
  }

  removeReceiptType(index: number) {
    const receiptLimitsFormArray = this.limitsForm.get('receiptLimits') as FormArray;
    receiptLimitsFormArray.removeAt(index);
  }

  onSubmit() {
    if (this.limitsForm.valid) {
  
      const modifiedLimitsConfiguration: LimitsConfiguration = this.limitsForm.value;
      this.limitsConfigService.saveLimitsConfiguration(modifiedLimitsConfiguration);
    }
  }

  get receiptTypes() {
    return this.limitsForm.get('receiptLimits') as FormArray;
  }
}

