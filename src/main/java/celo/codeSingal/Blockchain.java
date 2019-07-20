package celo.codeSingal;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Blockchain {
	public static String getLatestBlock(int[] startBalances,
			int[][] pendingTransactions, int blockSize) {
		String genesisHash = "0000000000000000000000000000000000000000";
		String resultPrefix = "0000";
		List<Block> blocks = new ArrayList<Block>();
		Block currentBlock = new Block();
		for (int[] tx : pendingTransactions) {
			if (startBalances[tx[0]] >= tx[2]) {
				currentBlock.validTransactions.add(tx);
				startBalances[tx[0]] -= tx[2];
				startBalances[tx[1]] += tx[2];
			}
			if (currentBlock.validTransactions.size() == blockSize) {
				blocks.add(currentBlock);
				currentBlock = new Block();
			}
		}
		if (currentBlock.validTransactions.size() > 0)
			blocks.add(currentBlock);
		List<String> serializedBlocks = new ArrayList<String>();
		for (int i = 0; i < blocks.size(); i++) {
			Block block = blocks.get(i);
			String txString = block.getTransactionsString();
			if (i == 0) {
				block.prevBlockHash = genesisHash;
			} else {
				block.prevBlockHash = blocks.get(i - 1).blockHash;
			}
			for (int nonce = 0; nonce < Integer.MAX_VALUE; nonce++) {
				block.nonce = nonce;
				String shaInput = block.getEncodedBlockForSha(txString);
				block.blockHash = sha1(shaInput);
				if (block.blockHash.startsWith(resultPrefix)) {
					serializedBlocks.add(block.getSerializedBlock(shaInput));
					break;
				}
			}
		}
		return serializedBlocks.get(serializedBlocks.size() - 1);
	}

	public static String sha1(String text) {
		// Sample implementation provided by code signal
		return "0000X";
	}

	public static void main(String args[]) {
		System.out.println(getLatestBlock(new int[] { 1, 7 },
				new int[][] { { 1, 0, 4 }, { 1, 0, 3 }, { 1, 0, 2 } }, 2));

		// getLatestBlock(new int[] { 5, 0, 0 },
		// new int[][] { { 0, 1, 5 }, { 1, 2, 5 } }, 2);
	}
}

class Block {
	List<int[]> validTransactions = new ArrayList<int[]>();
	int nonce;
	String blockHash;
	String prevBlockHash;

	String getEncodedBlockForSha(String txString) {
		String result = prevBlockHash + ", " + nonce + ", " + txString;
		return result;
	}

	String getTransactionsString() {
		String result = "[";
		for (int i = 0; i < validTransactions.size(); i++) {
			if (i > 0)
				result += ", ";
			result += Arrays.toString(validTransactions.get(i));
		}
		result += "]";
		return result;
	}

	String getSerializedBlock(String shaInput) {
		return blockHash + ", " + shaInput;
	}
}
