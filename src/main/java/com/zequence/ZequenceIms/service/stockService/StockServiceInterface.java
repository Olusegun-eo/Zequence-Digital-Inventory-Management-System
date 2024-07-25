package com.zequence.ZequenceIms.service.stockService;

import com.zequence.ZequenceIms.prodStocDaosDtos.StockDTO;

import java.util.List;

public interface StockServiceInterface {

    StockDTO addStock(StockDTO stockDTO);
    List<StockDTO> getAllStocks();
    StockDTO getStock(Long stockId) throws StockNotFoundException;
    StockDTO updateStock(Long stockId, StockDTO stockDTO) throws StockNotFoundException;
    void deleteStock(Long stockId) throws StockNotFoundException;
    //StockDTO addStockTransfer(StockTransferDTO stockTransferDTO);
    //List<StockTransferDTO> getStockTransfers(Long stockId) throws StockNotFoundException;
}
