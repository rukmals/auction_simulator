package org.auction_simulator;

public class PlaceBid implements AuctionCommand {
    public final String bidder;
    public final double amount;

    public PlaceBid(String bidder, double amount) {
        this.bidder = bidder;
        this.amount = amount;
    }
}
