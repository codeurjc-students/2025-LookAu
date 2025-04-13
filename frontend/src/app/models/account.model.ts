export interface Account{
  id: number;
  firstName: String;
  lastName: String;
  nickName: String;
  email: String;
  password: String;
  profilePicture: number[];
  
  pendingFriends: String[];
  requestFriends: String[];
  myFriends: String[];

  // teams: Team[];
  // tickets: Ticket[];

}
