package com.tongzhu.product_service;

// I want to pretend there is an IT operator here (or some! )
// how to persist error is not our main topic of interest...
// but I know there is no 100% pure automated eventual consistency, so,
// the safe net here is this, and human work...

import org.springframework.stereotype.Service;

@Service
public class PretendedErrorService {

    public static void pretendToPersistError(String uuid, String errorMessage) {

    }

}
