package org.auction_simulator;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

public class AuctionManager {
    private final Map<String, ActorRef<AuctionCommand>> auctions = new HashMap<>();

    public static Behavior<AuctionManagerCommand> create() {
        return Behaviors.setup(context -> new AuctionManager(context).behavior());
    }

    private final ActorContext<AuctionManagerCommand> context;

    private AuctionManager(ActorContext<AuctionManagerCommand> context) {
        this.context = context;
    }

    private Behavior<AuctionManagerCommand> behavior() {
        return Behaviors.receive(AuctionManagerCommand.class)
                .onMessage(CreateAuction.class, this::onCreateAuction)
                .onMessage(ForwardBid.class, this::onForwardBid)
                .onMessage(EndAuctionForManager.class, this::onEndAuctionForManager)
                .build();
    }

    private Behavior<AuctionManagerCommand> onCreateAuction(CreateAuction command) {
        ActorRef<AuctionCommand> auction = context.spawn(
                AuctionActor.create(command.auctionId, command.initialPrice),
                command.auctionId
        );
        auctions.put(command.auctionId, auction);
        context.getLog().info("Auction '{}' created with initial price: {}", command.auctionId, command.initialPrice);
        context.getSystem().scheduler().scheduleOnce(
                Duration.ofSeconds(command.durationInSeconds),
                () -> context.getSelf().tell(new EndAuctionForManager(command.auctionId)),
                context.getExecutionContext()
        );
        return Behaviors.same();
    }

    private Behavior<AuctionManagerCommand> onForwardBid(ForwardBid command) {
        ActorRef<AuctionCommand> auction = auctions.get(command.auctionId);
        if (auction != null) {
            auction.tell(new PlaceBid(command.placeBid.bidder, command.placeBid.amount));
            context.getLog().info("Forwarded bid from '{}' to auction '{}'", command.placeBid.bidder, command.auctionId);
        } else {
            context.getLog().info("Auction '{}' not found.", command.auctionId);
        }
        return Behaviors.same();
    }

    private Behavior<AuctionManagerCommand> onEndAuctionForManager(EndAuctionForManager command) {
        ActorRef<AuctionCommand> auction = auctions.get(command.auctionId);
        if (auction != null) {
            auction.tell(new EndAuction());
            context.getLog().info("Ended auction '{}'", command.auctionId);
            auctions.remove(command.auctionId); // Optionally remove the auction after ending it
        } else {
            context.getLog().info("Auction '{}' not found for ending.", command.auctionId);
        }
        return Behaviors.same();
    }

}
