export interface Account{
  id: number;
  firstName: string;
  lastName: string;
  nickName: string;
  email: string;
  password: string;
  profilePicture: number[];
  
  pendingFriends: string[];
  requestFriends: string[];
  myFriends: string[];

  teams: string[];
  // tickets: Number[];

}
