import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IBlock } from 'app/shared/model/block.model';

type EntityResponseType = HttpResponse<IBlock>;
type EntityArrayResponseType = HttpResponse<IBlock[]>;

@Injectable({ providedIn: 'root' })
export class BlockService {
    public resourceUrl = SERVER_API_URL + 'api/blocks';

    constructor(protected http: HttpClient) {}

    create(block: IBlock): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(block);
        return this.http
            .post<IBlock>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    update(block: IBlock): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(block);
        return this.http
            .put<IBlock>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http
            .get<IBlock>(`${this.resourceUrl}/${id}`, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IBlock[]>(this.resourceUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    protected convertDateFromClient(block: IBlock): IBlock {
        const copy: IBlock = Object.assign({}, block, {
            time: block.time != null && block.time.isValid() ? block.time.toJSON() : null
        });
        return copy;
    }

    protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
        if (res.body) {
            res.body.time = res.body.time != null ? moment(res.body.time) : null;
        }
        return res;
    }

    protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
        if (res.body) {
            res.body.forEach((block: IBlock) => {
                block.time = block.time != null ? moment(block.time) : null;
            });
        }
        return res;
    }
}
