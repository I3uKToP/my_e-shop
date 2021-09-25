import {Component, ElementRef, OnInit, ViewChild} from '@angular/core';
import {CartService} from "../../services/cart.service";
import {AllCartDto} from "../../model/all-cart-dto";
import {LineItem} from "../../model/line-item";
import {empty} from "rxjs";


export const CART_URL = 'cart'

@Component({
  selector: 'app-cart-page',
  templateUrl: './cart-page.component.html',
  styleUrls: ['./cart-page.component.scss']
})
export class CartPageComponent implements OnInit {
  @ViewChild('qtyInputHtml') qtyInputHtml?: number;
  content?: AllCartDto;


  constructor(private cartService: CartService) {
  }

  ngOnInit(): void {
    this.cartService.findAll().subscribe(
      res => {
        this.content = res;
      }
    )
  }

  removeItem(lineItem: LineItem) {
    this.cartService.removeItem(lineItem).subscribe(
      res=> {
        this.content = res;
      }
    );
  }

  changeQty(lineItem: LineItem) {
    this.cartService.changeQty(lineItem).subscribe(
      res=> {
        this.content = res;
      }
    );
  }

  clearCart() {
    this.content = undefined;
    this.cartService.clearCart().subscribe()
  }
}
