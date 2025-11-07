package uj.wmii.pwj.collections;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;

public final class BrainfuckInterpreter implements Brainfuck {

    private final String program;
    private final PrintStream out;
    private final InputStream in;
    private final byte[] memory;
    private int instructionPointer;


    public BrainfuckInterpreter(String program, PrintStream out, InputStream in, int stackSize) {
        this.program = program;
        this.out = out;
        this.in = in;
        this.memory = new byte[stackSize];
        this.instructionPointer = 0;
    }

    private void findBracketStart() {
        int excess = 0;
        while ( instructionPointer >= 0 ) {
            if ( program.charAt(instructionPointer) == ']' ) excess++;
            else
            if ( program.charAt(instructionPointer) == '[' ) excess--;

            if ( excess == 0 ) return;
            instructionPointer--;
        }
    }
    private void findBracketEnd() {
        int excess = 0;
        while ( instructionPointer >= 0 ) {
            if ( program.charAt(instructionPointer) == ']' ) excess--;
            else
            if ( program.charAt(instructionPointer) == '[' ) excess++;

            if ( excess == 0 ) return;
            instructionPointer++;
        }
    }

    public void execute() {
        int memoryPointer = instructionPointer = 0;
        while ( instructionPointer < program.length() ) {
            switch (program.charAt(instructionPointer)) {
                case '>':
                    memoryPointer++;
                    break;
                case '<':
                    memoryPointer--;
                    break;
                case '+':
                    memory[memoryPointer]++;
                    break;
                case '-':
                    memory[memoryPointer]--;
                    break;
                case '.':
                    out.print( (char)memory[memoryPointer] );
                    break;
                case ',':
                    int readInput;
                    try {
                        readInput = in.read();
                    }
                    catch ( IOException e ) {
                        break;
                    }
                    memory[memoryPointer] = (byte) readInput;
                    break;
                case '[':
                    if ( memory[memoryPointer] == 0 ) {
                        findBracketEnd();
                    }
                    break;
                case ']':
                    if ( memory[memoryPointer] != 0 ) {
                        findBracketStart();
                    }
                    break;
            }
            instructionPointer++;
        }
    }
    /*
    static Brainfuck createInstance(String program, PrintStream out, InputStream in, int stackSize) {
        return new BrainfuckInterpreter(program, out, in, stackSize);
    }
    */
}
