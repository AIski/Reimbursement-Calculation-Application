export class LimitsConfiguration {
    dailyAllowanceRate: number = 0;
    carMileageRate: number = 0;
    totalReimbursementLimit: number = 0;
    mileageLimitInKilometers: number = 0;
    receiptLimits: { [key: string]: number } = {};
}
