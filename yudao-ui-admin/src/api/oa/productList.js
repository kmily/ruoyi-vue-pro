import request from '@/utils/request'

// 创建产品清单列表
export function createProductList(data) {
  return request({
    url: '/oa/product-list/create',
    method: 'post',
    data: data
  })
}

// 更新产品清单列表
export function updateProductList(data) {
  return request({
    url: '/oa/product-list/update',
    method: 'put',
    data: data
  })
}

// 删除产品清单列表
export function deleteProductList(id) {
  return request({
    url: '/oa/product-list/delete?id=' + id,
    method: 'delete'
  })
}

// 获得产品清单列表
export function getProductList(id) {
  return request({
    url: '/oa/product-list/get?id=' + id,
    method: 'get'
  })
}

// 获得产品清单列表分页
export function getProductListPage(query) {
  return request({
    url: '/oa/product-list/page',
    method: 'get',
    params: query
  })
}

// 导出产品清单列表 Excel
export function exportProductListExcel(query) {
  return request({
    url: '/oa/product-list/export-excel',
    method: 'get',
    params: query,
    responseType: 'blob'
  })
}
