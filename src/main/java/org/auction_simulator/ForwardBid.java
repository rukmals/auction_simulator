package org.auction_simulator;

public class ForwardBid implements AuctionManagerCommand {
    public final String auctionId;
    public final PlaceBid placeBid;

    public ForwardBid(String auctionId, PlaceBid placeBid) {
        this.auctionId = auctionId;
        this.placeBid = placeBid;
    }
}

