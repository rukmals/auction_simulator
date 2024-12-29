package org.auction_simulator;

import akka.actor.typed.ActorSystem;

public class Main {
    public static void main(String[] args) {
        ActorSystem<AuctionManagerCommand> auctionManager =
                ActorSystem.create(AuctionManager.create(), "AuctionSystem");

        auctionManager.tell(new CreateAuction("auction-1", 100.0, 30));
//        auctionManager.tell(new CreateAuction("auction-2", 200.0, 30));

        auctionManager.tell(new ForwardBid("auction-1", new PlaceBid("Alice", 120.0)));
        auctionManager.tell(new ForwardBid("auction-1", new PlaceBid("Bob", 130.0)));
        auctionManager.tell(new ForwardBid("auction-1", new PlaceBid("Charlie", 110.0)));

        auctionManager.tell(new EndAuctionForManager("auction-1"));

        auctionManager.terminate();
    }

}