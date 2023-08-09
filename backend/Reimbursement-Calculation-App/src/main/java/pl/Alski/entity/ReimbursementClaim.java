package pl.Alski.entity;

import pl.Alski.entity.claims.ClaimStatus;

import java.time.LocalDateTime;

public abstract class ReimbursementClaim {
    private ClaimStatus status;
    private LocalDateTime createdAt;
}
