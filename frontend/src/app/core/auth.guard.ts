import { Injectable } from "@angular/core";
import { CanActivate, CanActivateChild, Router, UrlTree } from "@angular/router";
import { Observable, of } from "rxjs";
import { AuthService } from "../auth/auth.service";

@Injectable({ providedIn: 'root' })
export class AuthGuard implements CanActivate, CanActivateChild {
  constructor(private authService: AuthService, private router: Router) { }

  canActivate(): Observable<boolean | UrlTree> {
    return this.checkAuth();
  }

  canActivateChild(): Observable<boolean | UrlTree> {
    return this.checkAuth();
  }

  private checkAuth(): Observable<boolean | UrlTree> {
    if (this.authService.isAuthenticated()) {
      return of(true);
    } else {
      return of(this.router.parseUrl('/login'));
    }
  }
}
