package com.emiyaoj.service.domain.sandbox;

public enum FileErrorType {
    CopyInOpenFile,
    CopyInCreateFile,
    CopyInCopyContent,
    CopyOutOpen,
    CopyOutNotRegularFile,
    CopyOutSizeExceeded,
    CopyOutCreateFile,
    CopyOutCopyContent,
    CollectSizeExceeded
}
