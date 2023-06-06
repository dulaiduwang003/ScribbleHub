package com.cn.app.superbot.comment;

import org.springframework.stereotype.Component;

import java.util.Base64;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

/**
 * The type String compressor.
 *
 * @author 欧渐风.
 * @email 2074055628 @qq.com.
 */
@Component
@SuppressWarnings("all")
public class StringCompressor {
    /**
     * Compress string.
     *
     * @param input the input
     * @return the string
     */
    public String compress(String input) {
        byte[] inputBytes = input.getBytes();
        byte[] outputBytes = new byte[inputBytes.length];
        Deflater deflater = new Deflater();
        deflater.setInput(inputBytes);
        deflater.finish();
        int compressedDataLength = deflater.deflate(outputBytes);
        byte[] compressedBytes = new byte[compressedDataLength];
        System.arraycopy(outputBytes, 0, compressedBytes, 0, compressedDataLength);
        deflater.end();
        return Base64.getEncoder().encodeToString(compressedBytes);
    }

    /**
     * Decompress string.
     *
     * @param input the input
     * @return the string
     * @throws DataFormatException the data format exception
     */
    public String decompress(String input) throws DataFormatException {
        byte[] compressedBytes = Base64.getDecoder().decode(input);
        byte[] outputBytes = new byte[compressedBytes.length];
        Inflater inflater = new Inflater();
        inflater.setInput(compressedBytes);
        int decompressedDataLength;
        try {
            decompressedDataLength = inflater.inflate(outputBytes);
        } catch (DataFormatException e) {
            inflater.end();
            throw e;
        }
        inflater.end();
        return new String(outputBytes, 0, decompressedDataLength);
    }
}
