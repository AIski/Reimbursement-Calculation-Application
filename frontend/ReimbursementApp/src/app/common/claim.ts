import { CarMileage } from "./car-mileage";
import { DailyAllowance } from "./daily-allowance";
import { Receipt } from "./receipt";

export class Claim {
    id: number = 0;
    startDate: Date = new Date();
    endDate: Date = new Date();
    receipts: Receipt[] = [];
    dailyAllowance: DailyAllowance = new DailyAllowance();
    carMileage: CarMileage = new CarMileage();
    totalReimbursementAmount: number = 0;
    userId: number = 0;
}
