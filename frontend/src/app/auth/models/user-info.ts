export class UserInfo {
  username!: string;
  roles!: string[];

  constructor(username?: string, roles?: string[]) {
    this.username = username || '';
    this.roles = roles || [];
  }
}
