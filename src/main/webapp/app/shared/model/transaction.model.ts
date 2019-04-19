import { IOutput } from 'app/shared/model/output.model';
import { IInput } from 'app/shared/model/input.model';

export interface ITransaction {
    id?: number;
    doubleSpend?: number;
    blockHeight?: number;
    time?: number;
    lockTime?: number;
    relayedBy?: string;
    hash?: string;
    index?: number;
    version?: number;
    size?: number;
    blockId?: number;
    outputs?: IOutput[];
    inputs?: IInput[];
}

export class Transaction implements ITransaction {
    constructor(
        public id?: number,
        public doubleSpend?: number,
        public blockHeight?: number,
        public time?: number,
        public lockTime?: number,
        public relayedBy?: string,
        public hash?: string,
        public index?: number,
        public version?: number,
        public size?: number,
        public blockId?: number,
        public outputs?: IOutput[],
        public inputs?: IInput[]
    ) {}
}
