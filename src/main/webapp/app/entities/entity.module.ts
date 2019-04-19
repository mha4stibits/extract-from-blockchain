import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
    imports: [
        RouterModule.forChild([
            {
                path: 'block',
                loadChildren: './block/block.module#BlockChainBlockModule'
            },
            {
                path: 'transaction',
                loadChildren: './transaction/transaction.module#BlockChainTransactionModule'
            },
            {
                path: 'input',
                loadChildren: './input/input.module#BlockChainInputModule'
            },
            {
                path: 'output',
                loadChildren: './output/output.module#BlockChainOutputModule'
            }
            /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
        ])
    ],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class BlockChainEntityModule {}
