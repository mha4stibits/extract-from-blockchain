import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IOutput } from 'app/shared/model/output.model';

@Component({
    selector: 'jhi-output-detail',
    templateUrl: './output-detail.component.html'
})
export class OutputDetailComponent implements OnInit {
    output: IOutput;

    constructor(protected activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ output }) => {
            this.output = output;
        });
    }

    previousState() {
        window.history.back();
    }
}
