import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { Output } from 'app/shared/model/output.model';
import { OutputService } from './output.service';
import { OutputComponent } from './output.component';
import { OutputDetailComponent } from './output-detail.component';
import { OutputUpdateComponent } from './output-update.component';
import { OutputDeletePopupComponent } from './output-delete-dialog.component';
import { IOutput } from 'app/shared/model/output.model';

@Injectable({ providedIn: 'root' })
export class OutputResolve implements Resolve<IOutput> {
    constructor(private service: OutputService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IOutput> {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(
                filter((response: HttpResponse<Output>) => response.ok),
                map((output: HttpResponse<Output>) => output.body)
            );
        }
        return of(new Output());
    }
}

export const outputRoute: Routes = [
    {
        path: '',
        component: OutputComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Outputs'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/view',
        component: OutputDetailComponent,
        resolve: {
            output: OutputResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Outputs'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'new',
        component: OutputUpdateComponent,
        resolve: {
            output: OutputResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Outputs'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/edit',
        component: OutputUpdateComponent,
        resolve: {
            output: OutputResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Outputs'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const outputPopupRoute: Routes = [
    {
        path: ':id/delete',
        component: OutputDeletePopupComponent,
        resolve: {
            output: OutputResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Outputs'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
