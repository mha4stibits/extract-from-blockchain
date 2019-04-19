import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { BlockChainSharedModule } from 'app/shared';
import {
    InputComponent,
    InputDetailComponent,
    InputUpdateComponent,
    InputDeletePopupComponent,
    InputDeleteDialogComponent,
    inputRoute,
    inputPopupRoute
} from './';

const ENTITY_STATES = [...inputRoute, ...inputPopupRoute];

@NgModule({
    imports: [BlockChainSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [InputComponent, InputDetailComponent, InputUpdateComponent, InputDeleteDialogComponent, InputDeletePopupComponent],
    entryComponents: [InputComponent, InputUpdateComponent, InputDeleteDialogComponent, InputDeletePopupComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class BlockChainInputModule {}
