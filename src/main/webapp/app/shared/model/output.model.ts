export interface IOutput {
    id?: number;
    n?: number;
    value?: number;
    address?: string;
    txIndex?: number;
    script?: string;
    spent?: boolean;
    spentToAddress?: boolean;
    transactionId?: number;
}

export class Output implements IOutput {
    constructor(
        public id?: number,
        public n?: number,
        public value?: number,
        public address?: string,
        public txIndex?: number,
        public script?: string,
        public spent?: boolean,
        public spentToAddress?: boolean,
        public transactionId?: number
    ) {
        this.spent = this.spent || false;
        this.spentToAddress = this.spentToAddress || false;
    }
}
