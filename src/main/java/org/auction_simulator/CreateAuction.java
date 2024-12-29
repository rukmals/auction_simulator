package org.auction_simulator;



public class CreateAuction implements AuctionManagerCommand {
    public final String auctionId;
    public final double initialPrice;
    public final int durationInSeconds;

    public CreateAuction(String auctionId, double initialPrice, int durationInSeconds) {
        this.auctionId = auctionId;
        this.initialPrice = initialPrice;
        this.durationInSeconds = durationInSeconds;
    }
}

