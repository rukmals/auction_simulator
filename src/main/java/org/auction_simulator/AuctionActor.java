package org.auction_simulator;

import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;

public class AuctionActor {
    public static Behavior<AuctionCommand> create(String auctionId, double initialPrice) {
        return Behaviors.setup(context -> new AuctionActor(context, auctionId, initialPrice).behavior());
    }

    private final ActorContext<AuctionCommand> context;
    private final String auctionId;
    private double highestBid;

    private String winner;

    private AuctionActor(ActorContext<AuctionCommand> context, String auctionId, double initialPrice) {
        this.context = context;
        this.auctionId = auctionId;
        this.highestBid = initialPrice;
    }

    private Behavior<AuctionCommand> behavior() {
        return Behaviors.receive(AuctionCommand.class)
                .onMessage(PlaceBid.class, this::onPlaceBid)
                .onMessage(EndAuction.class, this::onEndAuction)
                .build();
    }

    private Behavior<AuctionCommand> onPlaceBid(PlaceBid bid) {
        if (bid.amount > highestBid) {
            highestBid = bid.amount;
            winner = bid.bidder;
            context.getLog().info("New highest bid: {} by {}", highestBid, bid.bidder);
        } else {
            context.getLog().info("Bid by {} is too low: {}", bid.bidder, bid.amount);
        }
        return Behaviors.same();
    }

    private Behavior<AuctionCommand> onEndAuction(EndAuction command) {
        context.getLog().info("Auction '{}' ended with highest bid: {} winner is : {}", auctionId, highestBid, winner);
        return Behaviors.stopped();
    }
}

