export const getProductId = product => product?.id ?? product?._id ?? product?.codigo ?? product?.nombre

export const normalizeProduct = product => ({
  ...product,
  id: getProductId(product),
})