import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { Input } from 'app/shared/model/input.model';
import { InputService } from './input.service';
import { InputComponent } from './input.component';
import { InputDetailComponent } from './input-detail.component';
import { InputUpdateComponent } from './input-update.component';
import { InputDeletePopupComponent } from './input-delete-dialog.component';
import { IInput } from 'app/shared/model/input.model';

@Injectable({ providedIn: 'root' })
export class InputResolve implements Resolve<IInput> {
    constructor(private service: InputService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IInput> {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(
                filter((response: HttpResponse<Input>) => response.ok),
                map((input: HttpResponse<Input>) => input.body)
            );
        }
        return of(new Input());
    }
}

export const inputRoute: Routes = [
    {
        path: '',
        component: InputComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Inputs'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/view',
        component: InputDetailComponent,
        resolve: {
            input: InputResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Inputs'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'new',
        component: InputUpdateComponent,
        resolve: {
            input: InputResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Inputs'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/edit',
        component: InputUpdateComponent,
        resolve: {
            input: InputResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Inputs'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const inputPopupRoute: Routes = [
    {
        path: ':id/delete',
        component: InputDeletePopupComponent,
        resolve: {
            input: InputResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Inputs'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
