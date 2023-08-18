import { Injectable } from '@angular/core';
import { LimitsConfiguration } from '../common/limits-configuration';
import { BehaviorSubject, Subject, firstValueFrom } from 'rxjs';
import { HttpClient, HttpHeaders, HttpResponse } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class LimitsConfigurationService {

  limitsConfiguration: Subject<LimitsConfiguration | null>  = new BehaviorSubject<LimitsConfiguration| null>(null)
  limitsConfigurationUrl: string = "http://localhost:8080/limits_config";

  constructor(
    private httpClient: HttpClient,
    ) { }

    async onInit(){
      try {
        const response = await this.getLimitsConfiguration();
        const config = response.body;
        this.limitsConfiguration.next(config);
      } catch (error) {
        console.error('Error fetching LimitsConfiguration:', error);
      }
    }

    getLimitsConfiguration(): Promise<HttpResponse<LimitsConfiguration>> {
      return firstValueFrom(
        this.httpClient.get<LimitsConfiguration>(this.limitsConfigurationUrl, { observe: 'response' }));
    }

    saveLimitsConfiguration(configuration: LimitsConfiguration) {
      // const jwtHeaders = new HttpHeaders({ 'Content-Type': 'application/json' });
      return firstValueFrom(this.httpClient.post<LimitsConfiguration>(this.limitsConfigurationUrl, configuration, { observe: 'response' }));
    }

}
