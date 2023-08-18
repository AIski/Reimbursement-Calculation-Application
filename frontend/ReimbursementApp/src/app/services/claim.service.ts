import { Injectable } from '@angular/core';
import { Observable } from 'rxjs/internal/Observable';
import { Claim } from '../common/claim';
import { firstValueFrom } from 'rxjs';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { ClaimRequest } from '../common/claim-request';

@Injectable({
  providedIn: 'root'
})
export class ClaimService {  
private baseUrl = 'http://localhost:8080';

constructor(private httpClient: HttpClient) {}

saveClaim(claimRequest: ClaimRequest): Promise<HttpResponse<Claim>> {
  const url = `${this.baseUrl}/claim`;
  return firstValueFrom(
    this.httpClient.post<Claim>(url, claimRequest, { observe: 'response' })
  );
}

getClaimsByUserId(userId: number): Observable<HttpResponse<Claim[]>> {
  const url = `${this.baseUrl}/claim?userId=${userId}`;
  return this.httpClient.get<Claim[]>(url, { observe: 'response' });
}
}





