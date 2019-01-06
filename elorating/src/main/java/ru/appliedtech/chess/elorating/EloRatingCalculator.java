package ru.appliedtech.chess.elorating;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import static java.lang.Math.abs;
import static java.util.Arrays.asList;

/**
 * https://www.fide.com/fide/handbook.html?id=197&view=article
 */
public class EloRatingCalculator {

    public EloRating newRating(EloRating playerRating, KValue kValue, EloRating opponentRating, BigDecimal score) {
        return playerRating
                .sum(ratingChange(playerRating, kValue, opponentRating, score));
    }

    public EloRatingChange ratingChange(EloRating playerRating, KValue kValue, EloRating opponentRating, BigDecimal score) {
        BigDecimal expectancy = scoringProbabilityByFIDEFormula(playerRating, opponentRating);
        BigDecimal increase = score.add(expectancy.negate()).multiply(new BigDecimal(kValue.value()));
        return new EloRatingChange(increase.setScale(2, BigDecimal.ROUND_HALF_UP));
    }

    private BigDecimal scoringProbabilityByFormula(EloRating playerRating, EloRating opponentRating) {
        BigDecimal numerator = BigDecimal.ONE.setScale(16, RoundingMode.HALF_UP);
        double normalizedRatingDifference = (opponentRating.getValue().doubleValue() - playerRating.getValue().doubleValue()) / 400.0;
        BigDecimal denominator = new BigDecimal(Math.pow(10.0, normalizedRatingDifference)).add(BigDecimal.ONE);
        return numerator.divide(denominator, RoundingMode.HALF_UP);
    }

    private BigDecimal scoringProbabilityByFIDEFormula(EloRating playerRating, EloRating opponentRating) {
        int difference = abs(playerRating.getValue().add(opponentRating.getValue().negate()).intValue());
        if (difference > 400) {
            difference = 400;
        }
        final int fdifference = difference;
        boolean high = playerRating.getValue().compareTo(opponentRating.getValue()) > 0;
        return SCORING_PROBABILITY_TABLE.stream()
                .filter(e -> fdifference >= e.getLeft() && fdifference <= e.getRight())
                .findFirst()
                .map(e -> high ? e.getH() : e.getL())
                .orElseThrow(() -> new IllegalArgumentException(
                        String.format("Player rating is %s and opponent rating is %s. No corresponding entry in scoring probability table is found",
                                playerRating, opponentRating)));
    }

    private static List<ScoringProbabilityTableEntry> SCORING_PROBABILITY_TABLE = asList(
            sp(0, 3,   new BigDecimal(0.50), new BigDecimal(0.50)),
            sp(4, 10,  new BigDecimal(0.51), new BigDecimal(0.49)),
            sp(11, 17, new BigDecimal(0.52), new BigDecimal(0.48)),
            sp(18, 25, new BigDecimal(0.53), new BigDecimal(0.47)),
            sp(26, 32, new BigDecimal(0.54), new BigDecimal(0.46)),
            sp(33, 39, new BigDecimal(0.55), new BigDecimal(0.45)),
            sp(40, 46, new BigDecimal(0.56), new BigDecimal(0.44)),
            sp(47, 43, new BigDecimal(0.57), new BigDecimal(0.43)),
            sp(54, 61, new BigDecimal(0.58), new BigDecimal(0.42)),
            sp(62, 68, new BigDecimal(0.59), new BigDecimal(0.41)),
            sp(69, 76, new BigDecimal(0.60), new BigDecimal(0.40)),
            sp(77, 83, new BigDecimal(0.61), new BigDecimal(0.39)),
            sp(84, 91, new BigDecimal(0.62), new BigDecimal(0.38)),

            sp(92,  98,  new BigDecimal(0.63), new BigDecimal(0.37)),
            sp(99,  106, new BigDecimal(0.64), new BigDecimal(0.36)),
            sp(107, 113, new BigDecimal(0.65), new BigDecimal(0.35)),
            sp(114, 121, new BigDecimal(0.66), new BigDecimal(0.34)),
            sp(122, 129, new BigDecimal(0.67), new BigDecimal(0.33)),
            sp(130, 137, new BigDecimal(0.68), new BigDecimal(0.32)),
            sp(138, 145, new BigDecimal(0.69), new BigDecimal(0.31)),
            sp(146, 153, new BigDecimal(0.70), new BigDecimal(0.30)),
            sp(154, 162, new BigDecimal(0.71), new BigDecimal(0.29)),
            sp(163, 170, new BigDecimal(0.72), new BigDecimal(0.28)),
            sp(171, 179, new BigDecimal(0.73), new BigDecimal(0.27)),
            sp(180, 188, new BigDecimal(0.74), new BigDecimal(0.26)),
            sp(189, 197, new BigDecimal(0.75), new BigDecimal(0.25)),

            sp(198, 206, new BigDecimal(0.76), new BigDecimal(0.24)),
            sp(207, 215, new BigDecimal(0.77), new BigDecimal(0.23)),
            sp(216, 225, new BigDecimal(0.78), new BigDecimal(0.22)),
            sp(226, 235, new BigDecimal(0.79), new BigDecimal(0.21)),
            sp(236, 245, new BigDecimal(0.80), new BigDecimal(0.20)),
            sp(246, 256, new BigDecimal(0.81), new BigDecimal(0.19)),
            sp(257, 267, new BigDecimal(0.82), new BigDecimal(0.18)),
            sp(268, 278, new BigDecimal(0.83), new BigDecimal(0.17)),
            sp(279, 290, new BigDecimal(0.84), new BigDecimal(0.16)),
            sp(291, 302, new BigDecimal(0.85), new BigDecimal(0.15)),
            sp(303, 315, new BigDecimal(0.86), new BigDecimal(0.14)),
            sp(316, 328, new BigDecimal(0.87), new BigDecimal(0.13)),
            sp(329, 344, new BigDecimal(0.88), new BigDecimal(0.12)),

            sp(345, 357, new BigDecimal(0.89), new BigDecimal(0.11)),
            sp(358, 374, new BigDecimal(0.90), new BigDecimal(0.10)),
            sp(375, 391, new BigDecimal(0.91), new BigDecimal(0.09)),
            sp(392, 411, new BigDecimal(0.92), new BigDecimal(0.08)),
            sp(412, 432, new BigDecimal(0.93), new BigDecimal(0.07)),
            sp(433, 456, new BigDecimal(0.94), new BigDecimal(0.06)),
            sp(457, 484, new BigDecimal(0.95), new BigDecimal(0.05)),
            sp(485, 517, new BigDecimal(0.96), new BigDecimal(0.04)),
            sp(518, 559, new BigDecimal(0.97), new BigDecimal(0.03)),
            sp(560, 619, new BigDecimal(0.98), new BigDecimal(0.02)),
            sp(620, 735, new BigDecimal(0.99), new BigDecimal(0.01)),
            sp(736, Integer.MAX_VALUE, new BigDecimal(1.00), new BigDecimal(0.00))
    );

    private static ScoringProbabilityTableEntry sp(int left, int right, BigDecimal h, BigDecimal l) {
        return new ScoringProbabilityTableEntry(left, right, h, l);
    }

    private static class ScoringProbabilityTableEntry {
        private final int left;
        private final int right;
        private final BigDecimal h;
        private final BigDecimal l;

        private ScoringProbabilityTableEntry(int left, int right, BigDecimal h, BigDecimal l) {
            this.left = left;
            this.right = right;
            this.h = h;
            this.l = l;
        }

        BigDecimal getH() {
            return h;
        }

        BigDecimal getL() {
            return l;
        }

        int getLeft() {
            return left;
        }

        int getRight() {
            return right;
        }
    }

}
