import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { BlockChainSharedModule } from 'app/shared';
import {
    OutputComponent,
    OutputDetailComponent,
    OutputUpdateComponent,
    OutputDeletePopupComponent,
    OutputDeleteDialogComponent,
    outputRoute,
    outputPopupRoute
} from './';

const ENTITY_STATES = [...outputRoute, ...outputPopupRoute];

@NgModule({
    imports: [BlockChainSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [OutputComponent, OutputDetailComponent, OutputUpdateComponent, OutputDeleteDialogComponent, OutputDeletePopupComponent],
    entryComponents: [OutputComponent, OutputUpdateComponent, OutputDeleteDialogComponent, OutputDeletePopupComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class BlockChainOutputModule {}
