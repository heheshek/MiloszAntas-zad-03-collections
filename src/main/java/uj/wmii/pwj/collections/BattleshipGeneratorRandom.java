package uj.wmii.pwj.collections;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

class Pair {
    public int x;
    public int y;
    public Pair( int setX, int setY ) {x = setX; y = setY;}
}

public class BattleshipGeneratorRandom implements BattleshipGenerator{
    private static int size = 10;
    private int[] ships;

    public String generateMap() {
        Random random = new Random();
        boolean[][] avalabileSpots = new boolean[size][size];
        for ( boolean[] row : avalabileSpots )
            Arrays.fill( row, true );
        boolean[][] shipSpots = new boolean[size][size];
        for ( boolean[] row : shipSpots )
            Arrays.fill( row, false );

        for( int ship : ships ) {
            int sizeCap = ship;
            Pair pickSpot;
            int tryPick = 0;
            while ( true ) {
                pickSpot = new Pair( random.nextInt(size), random.nextInt(size) );
                if ( avalabileSpots[pickSpot.y][pickSpot.x] ) {
                    int space = 1;
                    int r = ( sizeCap + 1 ) / 2; // integer division that rounds up

                    for (int dx = Math.max( 0, pickSpot.x - r ); dx <= Math.min( size - 1, pickSpot.x + r ) ; dx++ ) {
                        for (int dy = Math.max( 0, pickSpot.y - r ); dy <= Math.min( size - 1, pickSpot.y + r ); dy++ ) {
                            if ( avalabileSpots[dy][dx] ) space++;
                        }
                    }
                    if ( space > sizeCap ) break;
                }
                tryPick++;
                if ( tryPick > 999 ) throw new RuntimeException("Too many failures to place ship");
            }
            Pair[] pickedSpots = new Pair[sizeCap];
            pickedSpots[0] = new Pair( pickSpot.x, pickSpot.y );
            shipSpots[ pickedSpots[0].y ][ pickedSpots[0].x ] = true;

            ArrayList<Pair> possibleSpots = new ArrayList<>();
            if (!( pickedSpots[0].y - 1 < 0 ) && avalabileSpots[ pickedSpots[0].y - 1 ][ pickedSpots[0].x ])
                possibleSpots.add( new Pair(pickedSpots[0].x, pickedSpots[0].y - 1) );
            if ( (pickedSpots[0].y + 1 < size)  && avalabileSpots[ pickedSpots[0].y + 1 ][ pickedSpots[0].x ])
                possibleSpots.add( new Pair(pickedSpots[0].x, pickedSpots[0].y + 1) );
            if (!( pickedSpots[0].x - 1 < 0 ) && avalabileSpots[ pickedSpots[0].y ][ pickedSpots[0].x - 1 ])
                possibleSpots.add( new Pair(pickedSpots[0].x - 1, pickedSpots[0].y) );
            if ( (pickedSpots[0].x + 1 < size)  && avalabileSpots[ pickedSpots[0].y ][ pickedSpots[0].x + 1 ])
                possibleSpots.add( new Pair(pickedSpots[0].x + 1, pickedSpots[0].y) );

            int i = 1; sizeCap--;
            while ( sizeCap > 0 ) {
                int pickNextSpot = random.nextInt(possibleSpots.size());
                Pair nextSpot = possibleSpots.get(pickNextSpot);
                possibleSpots.remove( nextSpot );
                if ( !( shipSpots[ nextSpot.y ][ nextSpot.x ] ) && ( avalabileSpots[ nextSpot.y ][ nextSpot.x ] ) ) {
                    pickedSpots[i] = nextSpot;
                    i++;
                    sizeCap--;
                    shipSpots[ nextSpot.y ][ nextSpot.x ] = true;

                    // this below just checks spots that are orthogonally connected to the added if they're:
                    // 1) not out of bounds, 2) not blocked by another ship, 3) not a part of the same ship (this would theoretically only be possible for ships size >4)
                    // each of the 4 spots are added if they meet the criteria
                    if (!( nextSpot.y - 1 < 0 )&& avalabileSpots[ nextSpot.y - 1 ][ nextSpot.x ] && !shipSpots[ nextSpot.y - 1 ][ nextSpot.x ])
                        possibleSpots.add( new Pair(nextSpot.x, nextSpot.y - 1) );
                    if ( (nextSpot.y + 1 < size) && avalabileSpots[ nextSpot.y + 1 ][ nextSpot.x ] && !shipSpots[ nextSpot.y + 1 ][ nextSpot.x ])
                        possibleSpots.add( new Pair(nextSpot.x, nextSpot.y + 1) );
                    if (!( nextSpot.x - 1 < 0 )&& avalabileSpots[ nextSpot.y ][ nextSpot.x - 1 ] && !shipSpots[ nextSpot.y ][ nextSpot.x - 1 ])
                        possibleSpots.add( new Pair(nextSpot.x - 1, nextSpot.y) );
                    if ( (nextSpot.x + 1 < size) && avalabileSpots[ nextSpot.y ][ nextSpot.x + 1 ] && !shipSpots[ nextSpot.y ][ nextSpot.x + 1 ])
                        possibleSpots.add( new Pair(nextSpot.x + 1, nextSpot.y) );
                }
            }
            for ( Pair data : pickedSpots ) {
                for (int dx = -1; dx < 2; dx++) {
                    for (int dy = -1; dy < 2; dy++) {
                        if (data.y + dy >= 0 && data.y + dy < size && data.x + dx >= 0 && data.x + dx < size) {
                            avalabileSpots[data.y + dy][data.x + dx] = false;
                        }
                    }
                }
            }
        }
        StringBuilder result = new StringBuilder(size * size);

        for ( int y = 0; y < size; y++ ) {
            for ( int x = 0; x < size; x++ ) {
                result.append( shipSpots[y][x] ? "#" : "." );
            }
        }
        return result.toString();
    }

    public BattleshipGeneratorRandom() {
        ships = new int[] {4, 3, 3, 2, 2, 2, 1, 1, 1, 1};
    }
}
