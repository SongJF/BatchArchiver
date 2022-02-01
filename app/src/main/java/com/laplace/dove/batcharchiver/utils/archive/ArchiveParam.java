package com.laplace.dove.batcharchiver.utils.archive;

public class ArchiveParam {
    private char[] password;
    private String encryptionType;
    private String CompressLevel;

    public  ArchiveParam() {}

    public ArchiveParam(char[] password, String encryptionType, String compressLevel) {
        this.password = password;
        this.encryptionType = encryptionType;
        CompressLevel = compressLevel;
    }

    public String getEncryptionType() {
        return encryptionType;
    }

    /**
     * set encryption type
     * for zip4j: ZIP_STANDARD/AES
     * @param encryptionType encryptionType
     */
    public void setEncryptionType(String encryptionType) {
        this.encryptionType = encryptionType;
    }

    public char[] getPassword() {
        return password;
    }

    public void setPassword(char[] password) {
        this.password = password;
    }

    public String getCompressLevel() {
        return CompressLevel;
    }

    public void setCompressLevel(String compressLevel) {
        CompressLevel = compressLevel;
    }

    public boolean canEncrypt(){
        return password!=null && password.length > 0 && encryptionType != null && !encryptionType.isEmpty();
    }
}
