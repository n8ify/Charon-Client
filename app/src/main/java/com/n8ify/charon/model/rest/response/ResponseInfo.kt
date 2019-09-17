package com.n8ify.charon.model.rest.response

import com.n8ify.charon.constant.CharacterConstant
import com.n8ify.charon.constant.CodeConstant
import java.util.*

data class ResponseInfo(val id : String, val status : String, val code : Int = CodeConstant.NORMAL, val message : String = CharacterConstant.EMPTY, val timestamp : Date = Date(System.currentTimeMillis())) {

    companion object {

        // -----  Status ------ //

        /** Anything is fine, Process flow is completely success. */
        const val STATUS_SUCCESS = "Success"

        /** Something wrong occurred on this process. But the process flow is done. */
        const val STATUS_FAIL = "Fail"

        /** Somethings causing fetal error, Process flow is undone */
        const val STATUS_ERROR = "Error"

        /** Default status. Process may be both of 'Success' or 'Fail' but just like the function didn't set it.  */
        const val STATUS_UNDEFINED = "Undefined"

    }

}