import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IInput } from 'app/shared/model/input.model';

@Component({
    selector: 'jhi-input-detail',
    templateUrl: './input-detail.component.html'
})
export class InputDetailComponent implements OnInit {
    input: IInput;

    constructor(protected activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ input }) => {
            this.input = input;
        });
    }

    previousState() {
        window.history.back();
    }
}
