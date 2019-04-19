import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IOutput } from 'app/shared/model/output.model';

type EntityResponseType = HttpResponse<IOutput>;
type EntityArrayResponseType = HttpResponse<IOutput[]>;

@Injectable({ providedIn: 'root' })
export class OutputService {
    public resourceUrl = SERVER_API_URL + 'api/outputs';

    constructor(protected http: HttpClient) {}

    create(output: IOutput): Observable<EntityResponseType> {
        return this.http.post<IOutput>(this.resourceUrl, output, { observe: 'response' });
    }

    update(output: IOutput): Observable<EntityResponseType> {
        return this.http.put<IOutput>(this.resourceUrl, output, { observe: 'response' });
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<IOutput>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IOutput[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }
}
