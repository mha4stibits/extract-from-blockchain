import { NgModule } from '@angular/core';

import { BlockChainSharedLibsModule, JhiAlertComponent, JhiAlertErrorComponent } from './';

@NgModule({
    imports: [BlockChainSharedLibsModule],
    declarations: [JhiAlertComponent, JhiAlertErrorComponent],
    exports: [BlockChainSharedLibsModule, JhiAlertComponent, JhiAlertErrorComponent]
})
export class BlockChainSharedCommonModule {}
