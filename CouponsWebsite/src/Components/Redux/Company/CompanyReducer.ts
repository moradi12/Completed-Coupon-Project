// src/Components/Redux/Company/CompanyReducer.ts

import { Company } from "../../Models/Company";
import { Coupon } from "../../Models/Coupon";

export class CompanyState{
  coupons:Coupon[] = [];
}
export enum CompanyActionType {
  CLEAR_COMPANY_STATE = "CLEAR_COMPANY_STATE",
  ADD_COMPANY = "ADD_COMPANY",
}

export interface CompanyAction {
  type: CompanyActionType;
  payload?: any;
}

// Action Creators
export function clearCompanyStateAction(): CompanyAction {
  return { type: CompanyActionType.CLEAR_COMPANY_STATE };
}

export function addCompanyAction(company: Company[]): CompanyAction {
  return { type: CompanyActionType.ADD_COMPANY, payload: company };
}


// Reducer
export function CompanyReducer(currentState: CompanyState = new CompanyState(), action: CompanyAction): CompanyState {
  let newState = { ...currentState };

  switch (action.type) {
    case CompanyActionType.CLEAR_COMPANY_STATE:
      newState.coupons = [];
      break;
    case CompanyActionType.ADD_COMPANY:
      newState.coupons = action.payload;
      break;
  }

  return newState;
}
