package com.geldata.examples

import com.geldata.driver.GelClientPool

interface Example {
    suspend fun runAsync(clientPool: GelClientPool)
}