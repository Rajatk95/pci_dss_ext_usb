package com.astrel.io.atomic;

class Recover {
    public static void main(String[] args) throws Exception {
        if (args.length == 1) {
            Journal.playCrashCount = Integer.parseInt(args[0]);
        }
        TransactionManager tm = new TransactionManager("journal", "backup");
    }
}
		
