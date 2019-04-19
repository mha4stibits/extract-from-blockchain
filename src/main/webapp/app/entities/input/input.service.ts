import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IInput } from 'app/shared/model/input.model';

type EntityResponseType = HttpResponse<IInput>;
type EntityArrayResponseType = HttpResponse<IInput[]>;

@Injectable({ providedIn: 'root' })
export class InputService {
    public resourceUrl = SERVER_API_URL + 'api/inputs';

    constructor(protected http: HttpClient) {}

    create(input: IInput): Observable<EntityResponseType> {
        return this.http.post<IInput>(this.resourceUrl, input, { observe: 'response' });
    }

    update(input: IInput): Observable<EntityResponseType> {
        return this.http.put<IInput>(this.resourceUrl, input, { observe: 'response' });
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<IInput>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IInput[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }
}
