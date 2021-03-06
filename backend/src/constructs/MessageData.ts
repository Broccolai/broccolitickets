import Player from './Player';
import Ticket from './Ticket';

type MessageData = {
  server: string | null;
  ticket: Ticket;
  author: Player;
  action: string;
};

export default MessageData;
