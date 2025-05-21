export interface Ticket{
  id: number;
  type: string;
  date: string;
  statusName: string;
  statusPrice: string;
  claimedBy: string;
  paidByName: string;
  paidByPice: string;
  ticketTypeId: string;
  
  idAccountsAreBeingPaid: string[];
  idReimbursementAreReferenced: string[];

  isBalanced: boolean;
}