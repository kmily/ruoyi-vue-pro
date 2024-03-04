import request from '@/utils/request'

// 创建查询商品列
export function createYouyouGoodslist(data) {
  return request({
    url: '/steam/youyou-goodslist/create',
    method: 'post',
    data: data
  })
}

// 更新查询商品列
export function updateYouyouGoodslist(data) {
  return request({
    url: '/steam/youyou-goodslist/update',
    method: 'put',
    data: data
  })
}

// 删除查询商品列
export function deleteYouyouGoodslist(id) {
  return request({
    url: '/steam/youyou-goodslist/delete?id=' + id,
    method: 'delete'
  })
}

// 获得查询商品列
export function getYouyouGoodslist(id) {
  return request({
    url: '/steam/youyou-goodslist/get?id=' + id,
    method: 'get'
  })
}

// 获得查询商品列分页
export function getYouyouGoodslistPage(params) {
  return request({
    url: '/steam/youyou-goodslist/page',
    method: 'get',
    params
  })
}
// 导出查询商品列 Excel
export function exportYouyouGoodslistExcel(params) {
  return request({
    url: '/steam/youyou-goodslist/export-excel',
    method: 'get',
    params,
    responseType: 'blob'
  })
}