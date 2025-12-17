export class TokenValidation {
  [x: string]: any;
  isValid!: boolean;

  constructor(data?: Partial<TokenValidation>) {
    Object.assign(this, data);
  }
}
