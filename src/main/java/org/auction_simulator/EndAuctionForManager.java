package org.auction_simulator;

public class EndAuctionForManager implements AuctionManagerCommand {
    public final String auctionId;

    public EndAuctionForManager(String auctionId) {
        this.auctionId = auctionId;
    }
}
