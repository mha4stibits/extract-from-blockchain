import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { Block } from 'app/shared/model/block.model';
import { BlockService } from './block.service';
import { BlockComponent } from './block.component';
import { BlockDetailComponent } from './block-detail.component';
import { BlockUpdateComponent } from './block-update.component';
import { BlockDeletePopupComponent } from './block-delete-dialog.component';
import { IBlock } from 'app/shared/model/block.model';

@Injectable({ providedIn: 'root' })
export class BlockResolve implements Resolve<IBlock> {
    constructor(private service: BlockService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IBlock> {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(
                filter((response: HttpResponse<Block>) => response.ok),
                map((block: HttpResponse<Block>) => block.body)
            );
        }
        return of(new Block());
    }
}

export const blockRoute: Routes = [
    {
        path: '',
        component: BlockComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Blocks'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/view',
        component: BlockDetailComponent,
        resolve: {
            block: BlockResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Blocks'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'new',
        component: BlockUpdateComponent,
        resolve: {
            block: BlockResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Blocks'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/edit',
        component: BlockUpdateComponent,
        resolve: {
            block: BlockResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Blocks'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const blockPopupRoute: Routes = [
    {
        path: ':id/delete',
        component: BlockDeletePopupComponent,
        resolve: {
            block: BlockResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Blocks'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
