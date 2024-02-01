import request from '@/utils/request'

// 创建收藏品选择
export function createSelItemset(data) {
  return request({
    url: '/steam/sel-itemset/create',
    method: 'post',
    data: data
  })
}

// 更新收藏品选择
export function updateSelItemset(data) {
  return request({
    url: '/steam/sel-itemset/update',
    method: 'put',
    data: data
  })
}

// 删除收藏品选择
export function deleteSelItemset(id) {
  return request({
    url: '/steam/sel-itemset/delete?id=' + id,
    method: 'delete'
  })
}

// 获得收藏品选择
export function getSelItemset(id) {
  return request({
    url: '/steam/sel-itemset/get?id=' + id,
    method: 'get'
  })
}

// 获得收藏品选择列表
export function getSelItemsetList(params) {
  return request({
    url: '/steam/sel-itemset/list',
    method: 'get',
    params
  })
}
// 导出收藏品选择 Excel
export function exportSelItemsetExcel(params) {
  return request({
    url: '/steam/sel-itemset/export-excel',
    method: 'get',
    params,
    responseType: 'blob'
  })
}