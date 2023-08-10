package pl.Alski.entity.user;

public class LimitsConfiguration {

    private double dailyAllowanceRate;
    private double carMileageRate;
    private int totalReimbursementLimit;
    private int mileageLimitInKilometers;


    //think whether configuration should be global, or user-deppendent?
    //think how to solve calculation result per receipt type, how to store

    //think how to introduce multiple receipt types, each should have own limit
}
