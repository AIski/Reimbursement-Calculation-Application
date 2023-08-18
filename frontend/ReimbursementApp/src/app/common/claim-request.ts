import { Receipt } from "./receipt";

export class ClaimRequest {
    startDate: Date = new Date();
  endDate: Date = new Date();
  receipts: Receipt[] = [];
  reimbursedDaysWithAllowance: Date[] = [];
  distanceInKMForCarMileage: number = 0;
  carPlates: string = '';
  totalReimbursementAmount: number = 0;
  userId: number = 0;
}
