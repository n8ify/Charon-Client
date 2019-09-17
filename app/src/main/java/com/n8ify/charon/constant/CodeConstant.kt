package com.n8ify.charon.constant

object CodeConstant {

    /** Default code which define the body which response back have nothing to worry */
    const val NORMAL = 0

    /**
     * Any error code is 5 digits start with 9....
     * */
    object Error {

        /** Default code which define the body that response back should to worry but it's define as a global
         * (should replace with more specific code like <i>BaseException</i>'s code). */
        const val ERROR_GLOBAL = 90000

        /* -------------------------------------------------------- */
        /* Non-custom exception (Java exception, Library exception, etc) start with 90... */
        /* TODO : Insert more non-custom exception code here */

        /** Failed to obtain JDBC Connection; nested exception is com.mysql.cj.jdbc.exceptions.CommunicationsException
         * : Communications link failure. */
        const val ERROR_COMMUNICATION_LINK_FAILED = 90100

        /* -------------------------------------------------------- */
        /* Custom exception (extend BaseException) start with 91... */

        /*
        * Result not found.
        * Start with 911..
        * Short-code : RNF
        * */
        /** Global result not found exception = 91101 */
        const val ERROR_RESULT_NOT_FOUND_GLOBAL = 91101

        /** No result from database inquiry = 91102 */
        const val ERROR_RNF_NO_RESULT_ON_DATABASE = 91102

        /*
         * Result not found.
         * Start with 912..
         * Short-code : IDS
         * */
        /** Global illegal dao access exception = 91201 */
        const val ERROR_ILLEGAL_DAO_STATE_GLOBAL = 91201

        /** NamedJdbcTemplate instance was called while it's null = 91202 */
        const val ERROR_IDS_NAMED_JDBC_TEMPLATE_NOT_READY = 91202

        /*
        * Missing required attribute
        * Start with 913..
        * Short-code : MRA
        * */
        /** Global missing required attribute. */
        const val ERROR_MISSING_REQUIRED_ATTRIBUTE_GLOBAL = 91301

        /** Missing search attribute for database where-clause.  */
        const val ERROR_MRA_MISSING_WHERE_CLAUSE_ATTRIBUTE = 91302

        /* TODO : Insert more custom exception code here */
    }
}