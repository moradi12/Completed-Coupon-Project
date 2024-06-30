// adminActions.ts

import { AdminActionType } from "./AdminReducer";


export const addCompanyAction = (companyData: any) => ({
  type: AdminActionType.addCompany,
  payload: companyData,
});

export const deleteCompanyAction = (companyId: number) => ({
  type: AdminActionType.deleteCompany,
  payload: companyId,
});

export const updateCompanyAction = (companyData: any) => ({
  type: AdminActionType.updateCompany,
  payload: companyData,
});

export const getCompanyListAction = () => ({
  type: AdminActionType.getCompanyList,
});
